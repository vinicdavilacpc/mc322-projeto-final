

package com.agendajava.backend.model.procedures;

import com.agendajava.backend.model.Manager;import com.agendajava.backend.model.rooms.SurgeryRoom;import com.agendajava.backend.model.users.Doctor;import com.agendajava.backend.model.users.Patient;import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import java.time.Duration;import java.time.LocalDateTime;import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.anyInt;
    import static org.mockito.ArgumentMatchers.anyString;
    import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class SurgeryTest {

 @Mock         private Patient mockPatient;
 @Mock         private LocalDate mockLimitDate;
 @Mock         private Doctor mockSurgeon;
 @Mock         private SurgeryRoom mockRoom;

    private Surgery surgeryUnderTest;

@BeforeEach
void setUp() throws Exception {
            surgeryUnderTest = new Surgery("name",mockPatient,Manager.Specialty.ANESTESIOLOGIA,Manager.Priority.ELETIVA,false,Duration.ofDays(0L),0,Duration.ofDays(0L),mockLimitDate) ;
        surgeryUnderTest.setSurgeon(mockSurgeon);
        surgeryUnderTest.setRoom(mockRoom);
}
        
    @Test
    void testIsEmergency() throws Exception {
    // Setup
    // Run the test
 final boolean result =  surgeryUnderTest.isEmergency();

        // Verify the results
 assertThat(result).isFalse() ;
    }
                    
    @Test
    void testIsUrgency() throws Exception {
    // Setup
    // Run the test
 final boolean result =  surgeryUnderTest.isUrgency();

        // Verify the results
 assertThat(result).isFalse() ;
    }
                                @Test
    void testGetSpecialty() throws Exception {
         assertThat(surgeryUnderTest.getSpecialty()).isEqualTo(Manager.Specialty.ANESTESIOLOGIA) ;
    }
        @Test
    void testGetLimitDate() throws Exception {
         assertThat(surgeryUnderTest.getLimitDate()).isEqualTo(mockLimitDate) ;
    }
                    @Test
    void testNeedsICU() throws Exception {
         assertThat(surgeryUnderTest.needsICU()).isFalse() ;
    }
                    @Test
    void testGetICURecoverTime() throws Exception {
         assertThat(surgeryUnderTest.getICURecoverTime()).isEqualTo(Duration.ofDays(0L)) ;
    }
                    @Test
    void testGetClinicalPriority() throws Exception {
         assertThat(surgeryUnderTest.getClinicalPriority()).isEqualTo(0) ;
    }
                                
    @Test
    void testSetStart() throws Exception {
    // Setup
    // Run the test
 surgeryUnderTest.setStart(LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results
    }
            }

