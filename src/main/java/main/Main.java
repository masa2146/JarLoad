package main;

import io.hubbox.commons.Database_Helper;
import io.hubbox.commons.ModuleCommons;
import jar.JarLoader;
import jar.ParentFile;
import spark.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static spark.Service.ignite;
import static spark.Spark.*;

/**
 * @author fatih
 */
public class Main {
    Service http;

    public Main(){

    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.startServer();
        main.callStartFunc();
    }

    private void callStartFunc() {
        try {
            JarLoader.downloadJar(new URL("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"), ParentFile.getParenFilePath() + "/plugins");
            List<Class<?>> loadedClass = JarLoader.load(ParentFile.getParenFilePath() + "/plugins", "io.hubbox.api", ModuleCommons.class);
            loadedClass.forEach(aClass -> {
                try {
                    final ModuleCommons moduleCommons = (ModuleCommons) aClass.newInstance();
                    moduleCommons.onInit();
                    moduleCommons.onStart(http);
//                    Field mapfield = aClass.getDeclaredField("map");
//                    mapfield.setAccessible(true);
//                    HashMap<String, String> map = (HashMap<String, String>) mapfield.get(instance);
//                    System.out.println(map);
//                    for (Map.Entry<String, String> entry : map.entrySet()) {
//                        final Method method = aClass.getDeclaredMethod(entry.getValue(), Request.class, Response.class);
//                        Spark.get("/" + entry.getKey(), new Route() {
//                            public Object handle(Request request, Response response) throws Exception {
//                                return method.invoke(instance, request, response);
//                            }
//                        });
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error on load jar: " + e.getMessage());
        }
    }

    private void startServer() {
        http = ignite().port(8082);
        Database_Helper.init("jdbc:h2:~/hubbox");
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }
                    return "OK";
                });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        get("/main", (request, response) -> "main");
    }

}
