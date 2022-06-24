package com.example.ConfluentKafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DomainCrawlerService {

  private KafkaTemplate<String, Domain> kafkaTemplate;
  @Value("${io.confluent.developer.config.topic.name}")
	private String topicName;	

  public DomainCrawlerService(KafkaTemplate<String, Domain> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void crawl(String name) {

    Mono<DomainList> domainListMono = WebClient.create()
        .get()
        .uri("https://api.domainsdb.info/v1/domains/search?domain=" + name)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(DomainList.class);
   

    domainListMono.subscribe(domainList -> {
      domainList.domains
          .forEach(domain -> {
            kafkaTemplate.send(topicName, domain);
            System.out.println("Domain message" + domain.getDomain());
          });
    });

  }
}