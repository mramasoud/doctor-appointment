
# clinic managment

Patients will view allOfThe available time slots and will take on of them by entering their info.
To use the program, you can use the Postman collection (doctor-collection)in the project or use console .
if you want to use console  -> logging.level.root= off 

## Authors
- [@mramasoud](https://github.com/mramasoud/doctor-appointment)


## Features

- add doctor.
- show free appointment for doctor.
- delete appointment by doctor.
- reserve appointment by patient.
- show free doctor appointment for patient.


## API Reference
### doctor:
#### insert doctor:
```http
  Post api/v1/doctor/appointments/
```
| Parameter   | Type     | Description    |
|:------------| :------- |:---------------|
| `name`      | `String` | **Required**.  |
    
this api for save doctor:
    
    {
        "name":"doctorName"
    }
    
if save success:

    {
    "code": 200,
    "message": "doctor have been created."
    } 

if request is duplicate:

    {
    "code": 200,
    "message": "doctor have been created."
    }


#### doctor daily workTime:
```http
  Post /api/v1/doctor/appointments/time/schedule
```
| Parameter | Type      | Description     |
|:----------|:----------|:----------------|
| `name`    | `String`  | **Required**.   |
    
this api for doctor daily time schedule :
    
    {
    "dayOfMonth": "YYYY-MM-DD",
    "startTime": "HH:MM",
    "endTime": "HH:MM"
}
    
if success:

    {
    "code": 200,
    "message": "{numberOfTimeSlot} time period appointments have been created."
    } 

if request is duplicate:

    {
    "code": 409,
    "message": "duplicate time work in day:"
    }

#### doctorShowFreeTimes:
```http
  Get /api/v1/doctor/appointments/{YYYY-MM-DD}
```
| Parameter  | Type        | Description     |
|:-----------|:------------|:----------------|
| `date`     | `LocalDate` | **Required**.   |
    
this api for show doctor daily appointment :

    [
        {
        "digit": number of appointment,
        "startTime": "HH:MM",
        "endTime": "HH:MM",
        "status": "[empty,reserved,reserving]"
     }
    ]



if one time reserved by patient: 

    [
    {
    "digit": number of appointment,
    "startTime": "HH:MM",
    "endTime": "HH:MM",
    "name":"patient name"
    "phoneNumber":"patient number "
    "status": "[reserved]"

    }
    ]

if there is no appointments for view empty list showen.

#### doctor can delete appointment:
```http
  delete /api/v1/doctor/appointments/{number}/{YYYY-MM-DD}
```
| Parameter                    | Type                | Description     |
|:-----------------------------|:--------------------|:----------------|
| `numberOfAppointment , date` | `integer,LocalDate` | **Required**.   |
    
this api for delete doctor daily appointment :
if success:
    {
    "code": 202,
    "message": "appointment have been deleted."
    }

if  time reserved by patient: 

    {
    "code": 406,
    "message": "Appointment have been Reserved."
    }

if number of day is not valid :
    {
    "code": 404,
    "message": "Appointment Not Found."
    }

### patient:

###### Get all appointments
```http
  GET /api/v1/patient/appointments/
```
this api show All appointments:
 
 
    [
        {
        "digit": number of appointment,
        "startTime": "HH:MM",
        "endTime": "HH:MM",
        "status": "[empty,reserved,reserving]"
     }
    ]

if there is no appointments for view empty list showen.
#### Get appointments by phone number :
```http
  GET /api/v1/patient/appointments/{phonenumber}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `phoneNumber` | `string` | **Required**. for example: /api/v1/patient/appointments/09376710620 |
    
this api show patient appointment:
 
    {
    "digit": number of appointment,
    "startTime": "HH:MM",
    "endTime": "HH:MM",
    "status": "reserved"
    }
    

if there is no appointment for view empty showen.
#### reserving  appointment

```http
  Post /api/v1/patient/appointments/reserving
```
| Parameter                     | Type                | Description     |
|:------------------------------|:--------------------|:----------------|
| `dayOfMonth,appointmentDigit` | `LocalDate,integer` | **Required**.   |


    {
        "dayOfMonth": "YYYY-MM-DD",
        "appointmentDigit": numberOfAppointment
    }

if appointment reservig by other paitent :

    {
    errorMessage: Appointment is reserved. 
    }

if success:

    {
    appointmentNumber: 5 10:00 - 10:30    Do you want  to confirm appointment time ?
    }

if patient confirm, call to next api. but don't confirm call nuReserved 


#### unReserved appointment:
```http
  delete /api/v1/patient/appointments/unReserved/{numberOfAppointment}
```
| Parameter              | Type      | Description                |
|:-----------------------|:----------|:---------------------------|
| `numberOfAppointment ` | `integer` | **Required**.              |

this api for unReserved appointment by patient:
if success:

    {
    "code": 202,
    "message": "Appointment unReservation success."
    }

else :

    {
    "code": 406,
    "message": "Appointment unReservation fail."
    }


#### final reserving  appointment
```http
  Post /api/v1/patient/appointments/reserved
```
| Parameter                           | Type                    | Description     |
|:------------------------------------|:------------------------|:----------------|
| `name,phoneNumber,appointmentDigit` | `String,String,integer` | **Required**.   |


    {
    "name": "patientName",
    "phoneNumber": "patientPhone",
    "appoin


if success:

    {
    "digit": number of appointment,
    "startTime": "HH:MM",
    "endTime": "HH:MM",
    "status": "reserved",
    "patientName": "name",
    "patientPhoneNumber": "number"
    }
