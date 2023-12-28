package com.blubank.doctorappointment;

import com.blubank.doctorappointment.entity.Doctor;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class CacheService {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<Long,String> doctorMap = hazelcastInstance.getMap("doctorMap");

   public Doctor findDoctor(Long id){
        String name  = doctorMap.get(id);
        if(name!=null) {
            String[] split = name.split(",");
            return new Doctor(Long.valueOf(split[1]), split[0]);
        }
        throw new NotFoundException("doctor not found");
    }
    public void PutToDoctorMap(Long key,String value){
      doctorMap.put(key,value);
    }

    public void clear(){
       doctorMap.clear();
    }

}
