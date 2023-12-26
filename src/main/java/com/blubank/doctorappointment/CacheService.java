package com.blubank.doctorappointment;

import com.blubank.doctorappointment.entity.Doctor;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<Long,String> doctorMap = hazelcastInstance.getMap("doctorMap");

   public Doctor findDoctor(Long id){
        String name  = doctorMap.get(id);
        return new Doctor(id,name);
    }
    public void PutToDoctorMap(Long key,String value){
      doctorMap.put(key,value);
    }


}
