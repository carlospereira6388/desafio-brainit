package br.com.brainit.desafio.service;

import java.util.List;

import br.com.brainit.desafio.dto.AuthResponseDTO;
import br.com.brainit.desafio.dto.UserDataDTO;
import br.com.brainit.desafio.dto.UserResponseDTO;

public interface UserService {
	AuthResponseDTO login(String email, String password);
	AuthResponseDTO register(UserDataDTO userDataDTO);
    List<UserResponseDTO> getAllUsers();
}