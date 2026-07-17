

package com.agendajava.backend.model.procedures;

import com.agendajava.backend.model.users.Patient;import org.mockito.junit.jupiter.MockitoExtension;
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
class ProcedureTest {

 @Mock         private Patient mockPatient;

    private Procedure procedureUnderTest;

@BeforeEach
void setUp() throws Exception {
            procedureUnderTest = new Procedure("name",LocalDateTime.of(2020, 1, 1, 0, 0, 0),Duration.ofDays(0L),mockPatient) {};
}
                    @Test
    void testGetName() throws Exception {
         assertThat(procedureUnderTest.getName()).isEqualTo("name") ;
    }
                    @Test
    void testGetStarDateTime() throws Exception {
         assertThat(procedureUnderTest.getStarDateTime()).isEqualTo(LocalDateTime.of(2020, 1, 1, 0, 0, 0)) ;
    }
                    @Test
    void testGetDuration() throws Exception {
         assertThat(procedureUnderTest.getDuration()).isEqualTo(Duration.ofDays(0L)) ;
    }
        @Test
    void testGetPatient() throws Exception {
         assertThat(procedureUnderTest.getPatient()).isEqualTo(mockPatient) ;
    }
        
    @Test
    void testGetEndDateTime() throws Exception {
    // Setup
    // Run the test
 final LocalDateTime result =  procedureUnderTest.getEndDateTime();

        // Verify the results
 assertThat(result).isEqualTo( LocalDateTime.of(2020, 1, 1, 0, 0, 0) ) ;
    }
                    
    @Test
    void testOverlapsWith() throws Exception {
    // Setup
                final Duration dur = Duration.ofDays(0L);

    // Run the test
 final boolean result =  procedureUnderTest.overlapsWith(LocalDateTime.of(2020, 1, 1, 0, 0, 0),dur);

        // Verify the results
 assertThat(result).isFalse() ;
    }
            }

