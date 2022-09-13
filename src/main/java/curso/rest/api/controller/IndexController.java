package curso.rest.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //aqui define arquitetura REST
@RequestMapping(value = "/usuario")
public class IndexController {
	
	//Serviço Restful
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity init(@RequestParam (value = "nome", required = false) String nome) {
		System.out.println("Parametro recebido " + nome);
		return new ResponseEntity("Olá Usuário Spring Boot, seu nome é "+ nome, HttpStatus.OK);
	}
	
}
