package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogPessoal.Service.UsuarioServise;
import org.generation.blogPessoal.model.UsuarioModel;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioServise usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, "Root", "root@root.com", "rootroot", " ", ""));
	}
	
	@Test
	@DisplayName("Cadastrar Um Usuario")
	public void deveCriarUmUsuario() {
		
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel(0L, "Mariana Almeida",
				"Mariana_alm@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do Usuario")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new UsuarioModel (0L,
				"maria da Silva", "maria_silva@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel (0L,
				"maria da Silva", "maria_silva@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		
		ResponseEntity<UsuarioModel> corpoResposta =  testRestTemplate
				.exchange("/usuarios/cadastrar",HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um usuario")
	
	public void deveAtualizarUmUsuario() {
		
		Optional<UsuarioModel> usuarioCadastrado = usuarioService.cadastrarUsuario(new UsuarioModel(0L,
				"maria da Silva", "maria_silva@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		
		UsuarioModel usuarioUpdate = new UsuarioModel(usuarioCadastrado.get().getId(),
			"Risomar da Silva", "Risomar_silva@email.com.br", "Risomar12345678", "https://i.imgur.com/qZXsFyj.jpeg", "");
		
		
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(usuarioUpdate);
		ResponseEntity<UsuarioModel>corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, UsuarioModel.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test 
	@DisplayName("Listar todos os usuarios")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastrarUsuario(new UsuarioModel(0L,
				"Mariana lima", "mariana@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		
		usuarioService.cadastrarUsuario(new UsuarioModel(0L,
				"Matheus de oliveira", "matheus@email.com.br", "12345678", "https://i.imgur.com/qZXsFyj.jpeg", ""));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET,null, String.class);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
