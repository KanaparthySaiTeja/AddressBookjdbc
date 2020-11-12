import org.junit.Assert;
import org.junit.Test;
import java.util.List;
public class AddressBookTest {

    @Test
    public void MatchContactCount() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        List<Contact> addressBookContactList = addressBookSystem.readAddressBookData( AddressBookSystem.IOService.DB_IO );
        System.out.println(addressBookContactList);
        Assert.assertEquals(7,addressBookContactList.size());
    }

    @Test
    public void givenNewZipForContactSyncWithDataBase() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        List<Contact> employeePayrollData = addressBookSystem.readAddressBookData(AddressBookSystem.IOService.DB_IO);
        addressBookSystem.updateContactPhoneNo("Sai",503692);
        boolean result = addressBookSystem.checkAddressBookInsyncWithDB("Sai");
        Assert.assertTrue( result );
    }
    @Test
    public void givenAddressBookContactData_WhenRetrieveByCity_ShouldReturnProperValue() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        addressBookSystem.readAddressBookData(DB_IO);
        Map<String,Integer> countContactByCity = addressBookSystem.readContactCountByCity(DB_IO);
        Assert.assertTrue( countContactByCity.get("Mahabubnagar").equals( 2 )
                && countContactByCity.get( "Hosur" ).equals( 1 ) && countContactByCity.get( "Kurnool" ).equals( 1 ) && countContactByCity.get( "Kadapa" ).equals( 1 ));
    }
    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        addressBookSystem.readAddressBookData(DB_IO);
        addressBookSystem.addContactToAddressBook("ab","Bunny","Balajinagar","vizag","AP",Long.parseLong( "92512"),Long.parseLong( "9869854127" ),"xyz@gmail.com","family",LocalDate.now());
        boolean result = addressBookSystem.checkAddressBookInsyncWithDB("ab");
        Assert.assertTrue( result );
    }

}