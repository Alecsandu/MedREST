package com.example.medrest.controller;

import com.example.medrest.dto.DoctorDto;
import com.example.medrest.dto.PatientDto;
import com.example.medrest.dto.PrescriptionDto;
import com.example.medrest.mapper.DoctorMapper;
import com.example.medrest.mapper.PatientMapper;
import com.example.medrest.mapper.PrescriptionMapper;
import com.example.medrest.model.Doctor;
import com.example.medrest.model.Patient;
import com.example.medrest.model.Prescription;
import com.example.medrest.service.DoctorService;
import com.example.medrest.service.PatientService;
import com.example.medrest.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/patients")
public class PatientController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final DoctorService doctorService;

    public PatientController(@Autowired PatientService patientService,
                             @Autowired PrescriptionService prescriptionService,
                             @Autowired DoctorService doctorService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get information about all the patients",
            operationId = "getPatients",
            description = "This request provides data about each patient entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "patients were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PatientDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No patient entities are stored in the database"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientDto>> getPatients() {
        List<Patient> patientList = patientService.getAllPatients();
        return ResponseEntity.ok(patientList
                .stream()
                .map(PatientMapper::patientToPatientDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get patient using an id",
            operationId = "getPatient",
            description = "By using a valid id you can get the information about its corresponding patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "patient found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDto> getPatient(@PathVariable("id") Long id) {
        PatientDto patientDto = PatientMapper.patientToPatientDto(patientService.getPatientById(id));
        return ResponseEntity.ok(patientDto);
    }

    @Operation(summary = "Create a new patient",
            operationId = "createPatient",
            description = "By providing the basic values for a patient you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "patient was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPatient(@RequestBody @Valid PatientDto newPatient) {
        Patient toBeSavedPatient = new Patient(newPatient.getFirstName(), newPatient.getLastName(), newPatient.getPhoneNumber(), newPatient.getEmailAddress());
        Patient savedPatient = patientService.addPatient(toBeSavedPatient);
        URI uri = URI.create("api/patients/" + savedPatient.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a patient",
            operationId = "changePatient",
            description = "Change the information about a patient by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "patient was updated"),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Boolean isOperationSuccessful = patientService.updatePatient(id, patient);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a patient",
            operationId = "patchPatient",
            description = "A part of the information about patient can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "patient was patched"),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchPatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        Boolean isOperationSuccessful = patientService.patchPatient(id, patient);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a patient entity",
            operationId = "removePatient",
            description = "This endpoint removes a patient from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "patient was deleted"),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePatient(@PathVariable("id") Long id) {
        Boolean isOperationSuccessful = patientService.deletePatient(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "add prescriptions to a patient",
            operationId = "addPrescriptionToPatient",
            description = "Firstly give the id of the patient entity which you want to modify and" +
                    " secondly the id of the prescription that you want add give to the patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "prescription was assigned to the patient"),
            @ApiResponse(responseCode = "404", description = "patient or prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(path = "/{patientId}/prescriptions/{prescriptionId}")
    public ResponseEntity<Void> addPrescriptionToPatient(@PathVariable("patientId") Long patientId,
                                                         @PathVariable("prescriptionId") Long prescriptionId) {
        Patient existingPatient = patientService.getPatientById(patientId);
        Prescription existingPrescription = prescriptionService.getPrescriptionById(prescriptionId);
        existingPatient.addPrescriptions(existingPrescription);
        Boolean isOperationSuccessful = patientService.updatePatient(patientId, existingPatient);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the prescriptions that are given to the patient with given id",
            operationId = "getPrescriptionPatients",
            description = "By using a valid id you can get the information about its corresponding prescription")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "prescription's patients found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PatientDto.class)))}
            ),
            @ApiResponse(responseCode = "204", description = "patient has no prescriptions"),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(path = "/{id}/patients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrescriptionDto>> getPatientPrescriptions(@PathVariable("id") Long patientId) {
        Patient existingPatient = patientService.getPatientById(patientId);
        if (existingPatient.getPrescriptions().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            List<PrescriptionDto> patientDtoList = existingPatient.getPrescriptions().stream().map(PrescriptionMapper::prescriptionToPrescriptionDto).collect(Collectors.toList());
            return ResponseEntity.ok(patientDtoList);
        }
    }

    @Operation(summary = "Remove a prescription from a patient",
            operationId = "removePatientPrescription",
            description = "This endpoint removes a prescription from a patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "prescription was removed from patient"),
            @ApiResponse(responseCode = "404", description = "given patient or prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping(path = "/{patientId}/prescriptions/{prescriptionId}")
    public ResponseEntity<Void> removePatientPrescription(@PathVariable("patientId") Long patientId,
                                                          @PathVariable("prescriptionId") Long prescriptionId) {
        Patient patient = patientService.getPatientById(patientId);
        Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
        patient.removePrescriptions(prescription);
        Patient samePatient = patientService.addPatient(patient);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "appoint a patient to a doctor",
            operationId = "appointPatientToDoctor",
            description = "Firstly give the id of the patient entity which you want to modify and" +
                    " secondly the id of the prescription that you want add give to the patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "patient was assigned to the doctor"),
            @ApiResponse(responseCode = "404", description = "patient or doctor not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(path = "/{patientId}/doctors/{doctorId}")
    public ResponseEntity<Void> appointPatientToDoctor(@PathVariable("patientId") Long patientId,
                                                         @PathVariable("doctorId") Long doctorId) {
        Patient existingPatient = patientService.getPatientById(patientId);
        Doctor existingDoctor = doctorService.getDoctorById(doctorId);
        existingPatient.addDoctor(existingDoctor);
        Boolean isOperationSuccessful = patientService.patchPatient(patientId, existingPatient);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the doctors that the patient has appointments with",
            operationId = "getPatientAppointmentsWithDoctors",
            description = "By using a valid patient id you can get the information about its corresponding doctors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "patient appointments found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class)))}
            ),
            @ApiResponse(responseCode = "204", description = "patient has no doctor appointments"),
            @ApiResponse(responseCode = "404", description = "patient not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(path = "/{id}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DoctorDto>> getPatientAppointmentsWithDoctors(@PathVariable("id") Long patientId) {
        Patient existingPatient = patientService.getPatientById(patientId);
        if (existingPatient.getDoctors().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            List<DoctorDto> doctorDtoList = existingPatient.getDoctors().stream().map(DoctorMapper::doctorToDoctorDto).collect(Collectors.toList());
            return ResponseEntity.ok(doctorDtoList);
        }
    }

    @Operation(summary = "Remove a doctor appointment from a patient",
            operationId = "removePatientAppointment",
            description = "This endpoint removes a doctor appointment from a patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor appointment was removed from patient"),
            @ApiResponse(responseCode = "404", description = "Given patient or appointment not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @Transactional
    @DeleteMapping(path = "/{patientId}/doctors/{doctorId}")
    public ResponseEntity<Void> removePatientAppointment(@PathVariable("patientId") Long patientId,
                                                          @PathVariable("doctorId") Long doctorId) {
        Patient patient = patientService.getPatientById(patientId);
        Doctor doctor = doctorService.getDoctorById(doctorId);
        patient.removeDoctor(doctor);
        doctor.removePatient(patient);
        Boolean result = patientService.updatePatient(patientId, patient);
        Boolean res = doctorService.updateDoctor(doctorId, doctor);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
