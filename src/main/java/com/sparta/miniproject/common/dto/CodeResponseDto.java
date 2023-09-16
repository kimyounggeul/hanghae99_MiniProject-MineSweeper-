package com.sparta.miniproject.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder(toBuilder = true) @RequiredArgsConstructor
public class CodeResponseDto {
    private final String msg;
}