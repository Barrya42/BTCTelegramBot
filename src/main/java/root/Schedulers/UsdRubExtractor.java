package root.Schedulers;

import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class UsdRubExtractor implements ResponseExtractor<Double>
{
    @Override
    public Double extractData(ClientHttpResponse clientHttpResponse) throws IOException
    {
        String res = "";
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(clientHttpResponse.getBody(), Charset.defaultCharset()));
        //char[] buffer = new char[256];
        int readed = 0;
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        while (inputStreamReader.read(charBuffer)>0)
        {
            inputStreamReader.read(charBuffer);
            charBuffer.flip();
            while (charBuffer.hasRemaining())
            {
                res += charBuffer.get();
            }
            charBuffer.clear();
        }
        inputStreamReader.close();
        try
        {
            Map<String, Object> map = JsonParserFactory.getJsonParser()
                    .parseMap(res);
            Map<String,Object> valute= (Map<String,Object>) map.get("Valute");
            Map<String,Object> usd = (Map<String, Object>) valute.get("USD");
            return Double.parseDouble(usd.get("Value").toString());
        }
        catch (RuntimeException ignored)
        {
        }
        // TODO: 22.12.18  ответ прочитан теперь нужно извлеч доллар
        return null;
    }
}
