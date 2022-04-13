package br.senai.sp.cfp8.guidecar.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FirebaseUtil {

	// variavel para guardar as credenciais do firebase
	private Credentials credenciais;
	// variavel para acessar o historico
	private Storage storage;
	// constante para o nome do bucket
	// usando o final pq é constante
	private final String BUCKET_NAME = "gs://pointcar-eabc7.appspot.com";
	// constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a url
	// usando o %s pq usamos o string format
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;

	public FirebaseUtil() {

		// buscar as credenciais (arquivo json)
		Resource resource = new ClassPathResource("chavefirebase.json");
		// ler o arquivo para obter as credenciais

		try {
			// resource.getInputStream faz com que abra e leia o arquivo
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			// acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
		} catch (IOException e) {

			throw new RuntimeException(e.getMessage());

		}
	}
	
	public String uploadFile(MultipartFile arquivo) {
		
		// gera uma string aleatoria para o nome do arquico
		
		// gera um hash, uma string aleatoria
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		
		return nomeArquivo;
		
	}

	private String getExtensao(String nomeArquivo) {

		// retorna o trecho da string do ultimo . ate o fim (pega a extensao do arquivo)
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));

	}
	

}
