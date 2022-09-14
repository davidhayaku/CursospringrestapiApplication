package curso.rest.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.model.Usuario;
import curso.rest.api.repository.UsuarioRepository;


@RestController //aqui define arquitetura REST
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Serviço Restful
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable (value = "id")Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
}
