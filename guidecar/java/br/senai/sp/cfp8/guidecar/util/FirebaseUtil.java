package br.senai.sp.cfp8.guidecar.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

// faz com que a classe sirva para auxiliar um processo
// faz com que o srping reconheça ela ... para usarmos o autowired
@Service
public class FirebaseUtil {

	// variavel para guardar as credenciais do firebase
	private Credentials credenciais;
	// variavel para acessar o historico
	private Storage storage;
	// constante para o nome do bucket
	// usando o final pq é constante
	private final String BUCKET_NAME = "pointcar-eabc7.appspot.com";
	// constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a url
	// url
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
			System.out.println("ERROOOOOOO");
			throw new RuntimeException(e);

		}
	}

	// metodo responsavel por subir o arquivo para o firebase
	public String uploadFile(MultipartFile arquivo) throws IOException {

		// gera uma string aleatoria para o nome do arquivo

		// gera um hash, uma string aleatoria
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());

		// criando um blobId
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);

		// criando um blob Info a partir do blob ID

		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

		// manda o blobInfo para o Storage passando os bytes do arquivo para ele
		storage.create(blobInfo, arquivo.getBytes());

		// retornar a url para acessar o arquivo, e no lugar do %s passa o nome do
		// arquivo, que é um nome aleatorio
		return String.format(DOWNLOAD_URL, nomeArquivo);

	}

	private String getExtensao(String nomeArquivo) {

		// retorna o trecho da string do ultimo . ate o fim (pega a extensao do arquivo)
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));

	}

	// metodo que exclui a foto do firebase
	public void deletar(String nomeArquivo) {

		// retira o prefix e sufixo do nome do arquivo
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");

		// pega um blob atraves do arquivo
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		
		// deleta o arquivo
		
		storage.delete(blob.getBlobId());

	}

}
