package gov.govcircle;

import com.bloxbean.cardano.client.account.Account;
import io.adabox.client.OgmiosWSClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rest.koios.client.backend.factory.BackendService;
import rest.koios.client.backend.factory.impl.BackendServiceImpl;

import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GovCircleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GovCircleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("GovCircleApplication Is Here");

	}
}
