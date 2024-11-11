package pe.gob.bcrp.util;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

@Component
public class CaptchaServiceGenerate {

    //Creating Captcha en manejador de  letras
   /** public static Captcha createCaptcha(Integer width, Integer height) {

        return new Captcha.Builder(width, height)
                .addBackground(new GradiatedBackgroundProducer())
                .addText(new DefaultTextProducer(), new DefaultWordRenderer())
                .addNoise(new CurvedLineNoiseProducer())
                .build();
    }**7

    //Converting to binary String
   /** public static String encodeCaptcha(Captcha captcha) {
        String image = null;
        try {
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(),"jpg", bos);
            byte[] byteArray= Base64.getEncoder().encode(bos.toByteArray());
            image = new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }**/

   // Método para crear el captcha con una operación matemática
   public static Captcha createCaptcha(Integer width, Integer height) {
       // Generar dos números aleatorios para la operación matemática
       Random random = new Random();
       int num1 = random.nextInt(20);  // Número entre 0 y 9
       int num2 = random.nextInt(20);  // Número entre 0 y 9

       // Crear el texto de la operación, por ejemplo "3 + 7"
       String mathOperation = num1 + " + " + num2;
       int resultado = num1 + num2;

       // Guardar el resultado para su validación (puedes guardarlo en la sesión)
       // session.setAttribute("captchaResultado", resultado);

       // Crear un TextProducer personalizado que devuelva la operación matemática
       TextProducer mathTextProducer = () -> mathOperation;

       // Crear el captcha con el TextProducer personalizado
       return new Captcha.Builder(width, height)
               .addBackground(new GradiatedBackgroundProducer())
               .addText(mathTextProducer, new DefaultWordRenderer())
               .addNoise(new CurvedLineNoiseProducer())
               .build();
   }

    // Convertir el captcha a una cadena en base64 para mostrarlo en el frontend
    public static String encodeCaptcha(Captcha captcha) {
        String image = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(), "jpg", bos);
            byte[] byteArray = Base64.getEncoder().encode(bos.toByteArray());
            image = new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }


}
