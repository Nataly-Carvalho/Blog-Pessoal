package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.UsuarioModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment =  WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll(null);
		
		usuarioRepository.save(new UsuarioModel(0L,"maria carvalho","maria@email.com.br","12345678","https://i.imgur.com/qZXsFyj.jpeg"));
		
		usuarioRepository.save(new UsuarioModel(0L,"juliana silva","juliana@email.com.br","12345678","https://i.imgur.com/qZXsFyj.jpeg"));
		
		usuarioRepository.save(new UsuarioModel(0L,"Marcelo almeida","maarcelo@email.com.br","12345678","https://i.imgur.com/qZXsFyj.jpeg"));
		
		usuarioRepository.save(new UsuarioModel(0L,"Adriana silva","adriana123@email.com.br","12345678","https://i.imgur.com/qZXsFyj.jpeg"));
		
		usuarioRepository.save(new UsuarioModel(0L,"Paulo silva","Paulo_M@email.com.br","12345678","https://i.imgur.com/qZXsFyj.jpeg"));
	}
	
	@Test
	@DisplayName("Retornar 1 usuario")
	public void deveRetornarUmUsuario() {
		Optional<UsuarioModel> usuario = usuarioRepository.findByUsuario("maria@email.com.br");
		assertTrue(usuario.get().getUsuario().equals("maria@email.com.br"));
	}
	
	@Test
	@DisplayName("Retornar 3 usuario")
	public void deveRetornarTresUsuarios() {
		List<UsuarioModel> listadeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("silva");
		assertEquals(3, listadeUsuarios.size());
		assertTrue(listadeUsuarios.get(0).getNome().equals("Adriana silva"));
		assertTrue(listadeUsuarios.get(1).getNome().equals("juliana silva"));
		assertTrue(listadeUsuarios.get(2).getNome().equals("Paulo silva"));
		
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}

	
	

}
