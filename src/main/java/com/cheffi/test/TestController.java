package com.cheffi.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheffi.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestController {

	@Operation(security = { @SecurityRequirement(name = "session-token") })
	@GetMapping("/test")
	public ApiResponse<String> test(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if(header != null)
			return ApiResponse.success(header);
		return ApiResponse.success("hello world");
	}


}
