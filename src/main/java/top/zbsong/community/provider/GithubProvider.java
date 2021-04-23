package top.zbsong.community.provider;

import okhttp3.*;
import org.springframework.stereotype.Component;
import top.zbsong.dto.AccessTokenDTO;

import java.io.IOException;

/**
 * Create By songzb on 2021/4/23
 *
 * @author songzb
 */
@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =  response.body().string();
            System.out.println(string);
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}
