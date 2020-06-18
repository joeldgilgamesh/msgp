package com.sprintpay.minfi.msgp.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprintpay.minfi.msgp.client.AuthorizedUserFeignClient;
import com.sprintpay.minfi.msgp.service.dto.NotificationDTO;
import com.sprintpay.minfi.msgp.service.dto.TypeNotificationDTO;

@AuthorizedUserFeignClient(name = "spminfimsno")
@Service
public interface RESTClientNotificationService {	
	    @RequestMapping("/api/typeNotificationByLibelle/{libelle}")
	    TypeNotificationDTO getTypeNotification(@PathVariable("libelle") String libelle);
	    
	    @PostMapping("/api/type-notifications")
	    TypeNotificationDTO createTypeNotification(@RequestBody TypeNotificationDTO typeNotificationDTO );
	    
	    @PostMapping("/api/notifications")
	    @Async
	    NotificationDTO createNotification(@RequestBody NotificationDTO notificationDTO );
	    
}
