package curso.rest.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.api.model.Usuario;
import curso.rest.api.repository.UsuarioRepository;

@CrossOrigin(origins = "*")
@RestController //aqui define arquitetura REST
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Relatório de usuário
	@GetMapping(value = "/{id}/relatoriopdf", produces = "application/pdf")
	public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id")Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	//Serviço Restful versão 1
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	public ResponseEntity<Usuario> buscaV1(@PathVariable (value = "id")Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		System.out.println("Executando versão 1 da API");
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	//Serviço Restful versão 2
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
	public ResponseEntity<Usuario> buscaV2(@PathVariable (value = "id")Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		System.out.println("Executando versão 2 da API");
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>>usuario(){
		List<Usuario> list = (List<Usuario>)usuarioRepository.findAll();
		return new ResponseEntity<List<Usuario>>(list,HttpStatus.OK);
	}

	@PostMapping(value = "/",produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		for(int pos = 0; pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@PutMapping(value = "/",produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
		
		for(int pos = 0; pos < usuario.getTelefones().size();pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());
		if(!userTemporario.getSenha().equals(usuario.getSenha())) {//senhas diferentes
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {
		usuarioRepository.deleteById(id);
		return "ok";
	}
	
	
}
