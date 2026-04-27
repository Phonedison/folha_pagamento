package sistema.repository.dao;

import java.util.List;
import sistema.exception.CpfDuplicadoException;

public interface GenericDAO<T> {
    void salvar(T entidade);

    void atualizar(T entidade, int id, int opcao) throws CpfDuplicadoException;

    void excluir(int id);

    List<T> listarTodos(int opcao);
}
