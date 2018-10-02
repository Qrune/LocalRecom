package ExternalApis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TicketMasterAPITest {

	@Test
	public void testSearch() {
		TicketMasterAPI tmApi = new TicketMasterAPI();
        // Mountain View, CA
        // tmApi.queryAPI(37.38, -122.08);
        // London, UK
        // tmApi.queryAPI(51.503364, -0.12);
        // Houston, TX
        tmApi.queryAPI(29.682684, -95.295410);

	}

}
