package com.manuel.nanque.infrastucture.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import com.manuel.nanque.core.storage.StorageProperties;
import com.manuel.nanque.domain.service.FotoStorageService;

public class LocalFotoStorageService implements FotoStorageService {

//	@Value("${ifood-api.storage.local.diretorio-fotos}")
//	private Path diretorioFotos;

	@Autowired
	private StorageProperties storagePropreties;

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possivél armazenar arquivo.", e);
		}
	}

	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		Path arquivoPath = getArquivoPath(nomeArquivo);
		try {
			FotoRecuperada fotoRecupera = FotoRecuperada.builder()
					   .inputStream(Files.newInputStream(arquivoPath))
					   .build();
			return fotoRecupera;
		} catch (Exception e) {
			throw new StorageException("Não foi possivél recuperar arquivo.", e);
		}
	}

	@Override
	public void remover(String nomeArquivo) {

		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possivél excluir arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		return storagePropreties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
	}

}
