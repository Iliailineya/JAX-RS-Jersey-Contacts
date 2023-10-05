package org.example.app.service;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.app.entity.Contact;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("resource")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactServiceTest extends JerseyTest {

    private static final List<Contact> CONTACTS;

    static {
        CONTACTS = new ArrayList<>();
        CONTACTS.add(new Contact(1L, "Alice", "555 020 1234"));
        CONTACTS.add(new Contact(2L, "Bob", "555 020 2222"));
        CONTACTS.add(new Contact(3L, "Lucy", "555 020 3333"));
        CONTACTS.add(new Contact(4L, "Tom", "555 020 4444"));
    }
    @Override
    protected Application configure() {
        return new ResourceConfig(ContactService.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        // Настройка клиента, если требуется
    }

    @Test
    public void testAGetContacts() {
        Response response = target("/api/v1.0/contacts").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Contact> contacts = response.readEntity(new GenericType<>() {
        });

        // Проверка, что список контактов не пустой и содержит ожидаемое количество элементов
        assertNotNull(contacts);
        assertEquals(CONTACTS.size(), contacts.size());

        // Проверка, что каждый контакт из списка совпадает с ожидаемым контактом
        for (int i = 0; i < CONTACTS.size(); i++) {
            Contact expectedContact = CONTACTS.get(i);
            Contact actualContact = contacts.get(i);
            assertEquals(expectedContact.getId(), actualContact.getId());
            assertEquals(expectedContact.getName(), actualContact.getName());
            assertEquals(expectedContact.getPhone(), actualContact.getPhone());
        }
    }


    @Test
    public void testGetContactById() {
        long contactId = 2L;
        Response response = target("/api/v1.0/contacts/" + contactId).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Contact contact = response.readEntity(Contact.class);

        // Проверка, что контакт был успешно получен
        assertNotNull(contact);

        // Проверка, что данные контакта совпадают с ожидаемыми
        Contact expectedContact = CONTACTS.stream()
                .filter(c -> c.getId() == contactId)
                .findFirst()
                .orElse(null);
        assertNotNull(expectedContact);
        assertEquals(expectedContact.getId(), contact.getId());
        assertEquals(expectedContact.getName(), contact.getName());
        assertEquals(expectedContact.getPhone(), contact.getPhone());
    }


    @Test
    public void testCreateContact() {
        Contact newContact = new Contact(5L, "John", "555 092 6565");
        Response response = target("/api/v1.0/contacts")
                .request()
                .post(Entity.entity(newContact, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateContact() {
        Contact updatedContact = new Contact(2L, null, "555 092 6565");
        Response response = target("/api/v1.0/contacts/2")
                .request()
                .put(Entity.entity(updatedContact, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteContact() {
        Response response = target("/api/v1.0/contacts/3").request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}
