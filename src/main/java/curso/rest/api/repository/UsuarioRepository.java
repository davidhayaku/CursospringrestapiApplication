package curso.rest.api.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import curso.rest.api.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long> {
	
	

}
