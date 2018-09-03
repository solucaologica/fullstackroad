package br.com.ionic.api.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.ionic.api.domain.Cliente;
import br.com.ionic.api.domain.dto.ClienteDTO;
import br.com.ionic.api.domain.enums.TipoCliente;
import br.com.ionic.api.repository.ClienteRepository;
import br.com.ionic.api.service.exception.DataViolationException;
import br.com.ionic.api.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		return this.repository.findAll();
	}

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return this.repository.save(obj);
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = this.find(obj.getId());
		updateData(newObj, obj);
		return this.repository.save(obj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			this.repository.deleteById(id);
			;
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Não foi possível excluir o cliente");
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return this.repository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}

}
