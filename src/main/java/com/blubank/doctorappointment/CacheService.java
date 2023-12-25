package com.blubank.doctorappointment;

import com.blubank.doctorappointment.entity.Doctor;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, Long> doctorMap = hazelcastInstance.getMap("doctorMap");

   public Doctor findDoctor(String name){
        Long id = doctorMap.get(name);
        return new Doctor(id,name);
    }
    public void PutToDoctorMap(String key,Long value){
      doctorMap.put(key,value);
    }


}
