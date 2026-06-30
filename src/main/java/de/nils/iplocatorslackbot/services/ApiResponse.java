package de.nils.iplocatorslackbot.services;

import java.util.Optional;

public record ApiResponse<T>(int statusCode, T body) {
}
