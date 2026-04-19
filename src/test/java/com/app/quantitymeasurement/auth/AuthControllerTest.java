package com.app.quantitymeasurement.auth;

import com.app.quantitymeasurement.auth.controller.AuthController;
import com.app.quantitymeasurement.auth.dto.AuthResponse;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.dto.RegisterRequest;
import com.app.quantitymeasurement.auth.dto.UserProfileResponse;
import com.app.quantitymeasurement.auth.model.AuthProvider;
//import com.app.quantitymeasurement.auth.model.UserRole;
import com.app.quantitymeasurement.auth.service.AuthService;
import com.app.quantitymeasurement.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc unit tests for AuthController.
 *
 * @WebMvcTest starts ONLY the web layer (no DB, no real service).
 * @MockBean replaces AuthService with a Mockito mock.
 * @WithMockUser simulates an authenticated user for protected endpoints.
 */
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    // These beans are needed by SecurityConfig
    @MockBean
    private com.app.quantitymeasurement.auth.service.UserDetailsServiceImpl userDetailsService;
    @MockBean
    private com.app.quantitymeasurement.auth.filter.JwtAuthenticationFilter jwtAuthFilter;
    @MockBean
    private com.app.quantitymeasurement.auth.service.CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    private com.app.quantitymeasurement.auth.service.OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    @MockBean
    private com.app.quantitymeasurement.auth.service.OAuth2AuthenticationFailureHandler oauth2FailureHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthResponse mockAuthResponse;
    private UserProfileResponse mockProfile;

    @BeforeEach
    void setUp() {
        mockAuthResponse = AuthResponse.builder()
                .accessToken("mock.jwt.token")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .email("rahul@example.com")
                .name("Rahul Sharma")
//                .role(UserRole.ROLE_USER)
                .provider(AuthProvider.LOCAL)
                .build();

        mockProfile = UserProfileResponse.builder()
                .id(1L)
                .name("Rahul Sharma")
                .email("rahul@example.com")
//                .role(UserRole.ROLE_USER)s
                .provider(AuthProvider.LOCAL)
                .emailVerified(false)
                .build();
    }

    // ── POST /register ────────────────────────────────────────────────────────

    @Test
    void register_ValidRequest_Returns201WithToken() throws Exception {
        Mockito.when(authService.register(any(RegisterRequest.class)))
               .thenReturn(mockAuthResponse);

        RegisterRequest req = new RegisterRequest("Rahul Sharma",
                "rahul@example.com", "secret123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("mock.jwt.token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("rahul@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.provider").value("LOCAL"));
    }

    @Test
    void register_BlankName_Returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("", "rahul@example.com", "secret123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_InvalidEmail_Returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("Rahul", "not-an-email", "secret123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShortPassword_Returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("Rahul", "rahul@example.com", "123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── POST /login ───────────────────────────────────────────────────────────

    @Test
    void login_ValidCredentials_Returns200WithToken() throws Exception {
        Mockito.when(authService.login(any(LoginRequest.class)))
               .thenReturn(mockAuthResponse);

        LoginRequest req = new LoginRequest("rahul@example.com", "secret123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mock.jwt.token"))
                .andExpect(jsonPath("$.expiresIn").value(86400));
    }

    @Test
    void login_MissingEmail_Returns400() throws Exception {
        LoginRequest req = new LoginRequest("", "secret123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /google ───────────────────────────────────────────────────────────

    @Test
    void googleLoginInfo_Returns200WithUrl() throws Exception {
        mockMvc.perform(get("/api/v1/auth/google"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.googleLoginUrl")
                        .value("/oauth2/authorization/google"));
    }

    // ── GET /me (requires authentication) ────────────────────────────────────

    @Test
    @WithMockUser(username = "rahul@example.com", roles = "USER")
    void getMe_WithValidJwt_Returns200WithProfile() throws Exception {
        Mockito.when(authService.getCurrentUser("rahul@example.com"))
               .thenReturn(mockProfile);

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("rahul@example.com"))
                .andExpect(jsonPath("$.name").value("Rahul Sharma"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void getMe_WithoutJwt_Returns401() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    // ── GET /admin/users ──────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void getAllUsers_WithAdminRole_Returns200() throws Exception {
        Mockito.when(authService.getAllUsers())
               .thenReturn(java.util.List.of(mockProfile));

        mockMvc.perform(get("/api/v1/auth/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("rahul@example.com"));
    }

    @Test
    @WithMockUser(username = "rahul@example.com", roles = "USER")
    void getAllUsers_WithUserRole_Returns403() throws Exception {
        mockMvc.perform(get("/api/v1/auth/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsers_WithoutJwt_Returns401() throws Exception {
        mockMvc.perform(get("/api/v1/auth/admin/users"))
                .andExpect(status().isUnauthorized());
    }
}
