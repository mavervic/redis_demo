package example.base64;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Example {

	public static void main(String[] args) throws IOException {

		// Encode
		byte[] imageBytes = Files.readAllBytes(Paths.get("src/example/base64/captcha.png"));
		String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
		System.out.println("被字串化的圖片" + encodedImage);

		// Decode
		byte[] decodedImage = Base64.getDecoder().decode(encodedImage);
		Files.write(Paths.get("src/example/base64/restore.png"), decodedImage);
	}

}
