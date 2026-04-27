package sistema.repository.dao;

import java.util.List;
import sistema.exception.CpfDuplicadoException;

/* 
* Interface para ser utilizado nas camadas DAO de Dependente, Funcionário e FolhaPagamento
* o <T> permite que a interface seja reutilizada na passagem de listas
*/
public interface GenericDAO<T> {

    void salvar(T entidade);

    void atualizar(T entidade, int id, int opcao) throws CpfDuplicadoException;

    void excluir(int id);

    List<T> listarTodos(int opcao);
}
