package curso.rest.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
	
	
	private static final long EXPIRATION_TIME = 172800000; 	//TEMPO DE VALIDADE DO TOKEN 2 DIAS
	private static final String SECRET = "SenhaExtremamenteSecreta";//UMA SENHA UNICA PARA COMPOR A AUTENTICACAO AJUDANDO A SEGURANÇA
	private static final String TOKEN_PREFIX = "Bearer";//PREFIXO PADRÃO DO TOKEN
	private static final String HEADER_STRING = "Authorization";
	
	//Gerando token de autenticação e adicionando ao cabeçalho e resposta http
	public void addAuthentication(HttpServletResponse response, String username)
	throws IOException{
		String JWT = Jwts.builder() //chama o gerador de token
		.setSubject(username)//Add o usuario
		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//tempo de expiração
		.signWith(SignatureAlgorithm.HS512, SECRET).compact();//compactação e algoritmos de geração de senha
		
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX + "" + JWT; //Bearer forma o token
		
		//Adiciona no cabeçalho http
		response.addHeader(HEADER_STRING, token); //Authorization: Bearer 8w7e8w78eq78w7
		
		//Escreve token como resposta no corpo http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");	
		}
	
	//Retorna o usuário validado com token ou no caso não seja válido retorna null
	public Authentication getautAuthentication(HttpServletRequest request) {
		
		//Pega o token enviado no cabeçalho Http
		String token = request.getHeader(HEADER_STRING);
		if(token != null) {
			//valida o token do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET) //recebe o token com prefixo
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))// retira o prefixo do token
					.getBody().getSubject(); //retorna só o usuário
			
		if(user != null) {
			Usuario usuario = ApplicationContextLoad.getApplicationContext()
					.getBean(UsuarioRepository.class).findUserByLogin(user);
			if(usuario != null) {
				
				return new UsernamePasswordAuthenticationToken(
						usuario.getLogin(), 
						usuario.getSenha(),
						usuario.getAuthorities()
						);				
			}			
		}		
		}
		return null;
		
	}
	

	
}
