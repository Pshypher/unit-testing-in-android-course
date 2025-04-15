package com.techyourchance.testdrivendevelopment.exercise7;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    // region constants
    public static final int REPUTATION = 10;
    public static final int NO_REPUTATION = 0;
    // endregion constants

    // region helper fields
    @Mock
    GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;
    // endregion helper fields

    FetchReputationUseCaseSync SUT;

    @Before
    public void setup() {
        SUT = new FetchReputationUseCaseSync(mGetReputationHttpEndpointSyncMock);
        success();
    }
    @Test
    public void getReputationSync_getReputationEndpointMethodCalled() {
        SUT.getReputationSync();
        verify(mGetReputationHttpEndpointSyncMock).getReputationSync();
    }

    @Test
    public void getReputationSync_success_successReturned() {
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.UseCaseStatus.SUCCESS));
    }

    @Test
    public void getReputationSync_generalError_failureReturned() {
        generalError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.UseCaseStatus.FAILURE));
    }

    @Test
    public void getReputationSync_networkError_failureReturned() {
        networkError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getStatus(), is(FetchReputationUseCaseSync.UseCaseStatus.FAILURE));
    }

    @Test
    public void getReputationSync_success_reputationReturned() {
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getReputation(), is(REPUTATION));
    }

    @Test
    public void getReputationSync_generalError_zeroReputationReturned() {
        generalError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getReputation(), is(NO_REPUTATION));
    }

    @Test
    public void getReputationSync_networkError_zeroReputationReturned() {
        networkError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result.getReputation(), is(NO_REPUTATION));
    }

    // region helper methods
    private void success() {
        when (mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, NO_REPUTATION));
    }

    private void networkError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, NO_REPUTATION));
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}