package danekerscode.keremetchat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class ExecutorServiceConfig {

    public static final int N_THREADS = 10;

    @Bean
    ExecutorService executorService(){
        var executorService = Executors.newFixedThreadPool(N_THREADS);//todo move to end amount of threads

        log.info("ExecutorService with fixed thread pool {} initialized" , N_THREADS);

        return executorService;
    }
}
