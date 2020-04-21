package com.sprint.minfi.msgp.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

public class EvenementListener<T> {
    private final Logger log = LoggerFactory.getLogger(EvenementListener.class);
    @Autowired
    private KafkaTemplate<String, Evenement> kafkaTemplate;
    @Value("${kafka.servers.topic.database:evenement_database}")
    private String topic;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @PostPersist
    public void entityPostPersist(T entity) {
        Evenement<T> evenement=new Evenement<T>();
        evenement.setValeur(entity);
        evenement.setDateEvt(LocalDateTime.now());
        evenement.setTypeEvenement("CREATE");
        evenement.setTable_name(entity.getClass().getSimpleName());
        kafkaTemplate.send(topic,applicationName+evenement.getDateEvt(),evenement);
        log.debug("Evenement créé et transmi au brooker"+evenement);
        System.out.println("Listening entity Post Persist : " + evenement);
    }

   /* @PostUpdate
    public void entityPostUpdate(T entity) {
        Evenement<T> evenement=new Evenement<T>();
        evenement.setValeur(entity);
        evenement.setDateEvt(LocalDateTime.now());
        evenement.setTypeEvenement("UPDATE");
        evenement.setTable_name(entity.getClass().getSimpleName());
        kafkaTemplate.send(topic,applicationName+evenement.getDateEvt(),evenement);
        log.debug("Evenement créé et transmi au brooker"+evenement);
        System.out.println("Listening entity Post Update : " + evenement);
    }*/

   /* @PostRemove
    public void entityPostRemove(T entity) {
        log.debug(" *************entité à envoyer au brooker*********"+entity);
        Evenement<T> evenement=new Evenement<T>();
        evenement.setValeur(entity);
        evenement.setDateEvt(LocalDateTime.now());
        evenement.setTypeEvenement("DELETE");
        evenement.setTable_name(entity.getClass().getSimpleName());
        kafkaTemplate.send(topic,applicationName+evenement.getDateEvt(),evenement);
        log.debug("Evenement créé et transmi au brooker"+evenement);
        System.out.println("Listening entity Post Remove : " + evenement);
    }*/

    String eventSerializer(Object event){
        JsonMapper jsonMapper=new JsonMapper();
        String result=null;
        try {
            result=jsonMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}