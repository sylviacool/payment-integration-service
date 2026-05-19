package com.paymentintegration.service.interfaces;

import com.paymentintegration.dto.request.AuthRequest;
import com.paymentintegration.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest request);
}