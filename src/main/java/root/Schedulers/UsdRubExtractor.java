package root.Schedulers;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class UsdRubExtractor implements ResponseExtractor<Double>
{
    @Override
    public Double extractData(ClientHttpResponse clientHttpResponse) throws IOException
    {
        String res = "";
        InputStreamReader inputStreamReader = new InputStreamReader(clientHttpResponse.getBody(), Charset.defaultCharset());
        char[] buffer = new char[256];
        while (inputStreamReader.ready())
        {
            int i = inputStreamReader.read(buffer);
            if (i > 0)
            {
                res += String.valueOf(buffer);
            }
        }
        // TODO: 22.12.18  ответ прочитан теперь нужно извлеч доллар
        return null;
    }
}
