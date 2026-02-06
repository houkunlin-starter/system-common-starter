package com.houkunlin.common.aop;

import com.houkunlin.common.aop.annotation.RequestRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RequestRateLimiterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @LocalServerPort
    private int port;

    @Test
    void m11() throws Exception {
        doRequest("/RequestRateLimiter/m11");
    }

    @Test
    void m12() throws Exception {
        doRequest("/RequestRateLimiter/m12");
    }

    @Test
    void m21() throws Exception {
        doRequest("/RequestRateLimiter/m21");
    }

    @Test
    void m22() throws Exception {
        doRequest("/RequestRateLimiter/m22");
    }

    @Test
    void m31() throws Exception {
        doRequest("/RequestRateLimiter/m31");
    }

    @Test
    void m32() throws Exception {
        doRequest("/RequestRateLimiter/m32");
    }

    void doRequest(String path) throws Exception {
        for (int i = 0; i < 120; i++) {
            mockMvc.perform(get(path))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(RequestRateLimiter.DEFAULT_MESSAGE));
    }
}
