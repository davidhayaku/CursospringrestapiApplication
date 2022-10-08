package curso.rest.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.rest.api.ApplicationContextLoad;
import curso.rest.api.model.Usuario;
import curso.rest.api.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	//TEMPO DE VALIDADE DO TOKEN 2 DIAS
	private static final long EXPIRATION_TIME = 172800000; 
	//UMA SENHA UNICA PARA COMPOR A AUTENTICACAO AJUDANDO A SEGURANÇA
	private static final String SECRET = "SenhaExtremamenteSecreta";
	//PREFIXO PADRÃO DO TOKEN
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	//Gerando token de autenticação e adicionando ao cabeçalho e resposta http
	public void addAuthentication(HttpServletResponse response, String username)
	throws IOException{
		String JWT = Jwts.builder() //chama o gerador de token
		.setSubject(username)//Add user
		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//tempo de expiração
		.signWith(SignatureAlgorithm.HS512, SECRET).compact();//compactação e algoritmos de geração
		
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX + "" + JWT; //Bearer forma o token
		
		//Adiciona no cabeçalho http
		response.addHeader(HEADER_STRING, token); //Authorization: Bearer 8w7e8w78eq78w7
		
		//Escreve token como resposta no corpo http
		response.getWriter().write("{\"Authorization\": \"" +token+"\"}");		
	}
	
	//Retorna o usuário validado com token ou caso não seja válido retorna null
	public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
	
		//Pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			//Faz validação no token do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET)//Bearer 9898s9A8S9A8SA98S9
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))//9898s9A8S9A8SA98S9
					.getBody().getSubject();//João Silva
		
		if(user != null) {
			
			Usuario usuario = ApplicationContextLoad.getApplicationContext()
					.getBean(UsuarioRepository.class).findUserByLogin(user);
			
			//Retornar usuario logado			
			if(usuario != null) {
				return new UsernamePasswordAuthenticationToken(
						usuario.getLogin(), 
						usuario.getSenha(),
						usuario.getAuthorities());
			}	
		}
	}
	return null; //Não autorizado
		
	}	
}
