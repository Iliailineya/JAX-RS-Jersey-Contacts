package org.example.app.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.app.entity.Contact;
import org.example.app.utils.AppStarter;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@Path("/api/v1.0/contacts")
@Produces({MediaType.APPLICATION_JSON})
public class ContactService {

    private static final Logger logger = Logger.getLogger(AppStarter.class.getName());

    // В реальній програмі використовується БД.
    // Для навчальних цілей застосовано статичний список.
    private static final List<Contact> CONTACTS;

    static {
        CONTACTS = new ArrayList<>();
        CONTACTS.add(new Contact(1L, "Alice", "555 020 1234"));
        CONTACTS.add(new Contact(2L, "Bob", "555 020 2222"));
        CONTACTS.add(new Contact(3L, "Lucy", "555 020 3333"));
        CONTACTS.add(new Contact(4L, "Tom", "555 020 4444"));
    }

    @GET
    public List<Contact> getContacts() {
        logger.info("GET request received for contacts.");
        return CONTACTS;
    }

    @GET
    @Path("{id: [0-9]+}")
    public Contact getContact(@PathParam("id") Long id) {
        logger.info("GET request received for contact with ID: " + id);
        Contact contact = new Contact(id, null, null);

        int index = Collections.binarySearch(CONTACTS, contact, Comparator.comparing(Contact::getId));

        if (index >= 0) {
            logger.info("Contact found with ID: " + id);
            return CONTACTS.get(index);
        } else {
            logger.warning("Contact not found with ID: " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createContact(Contact contact) {
        logger.info("POST request received to create contact with ID: " + contact.getId());

        if (Objects.isNull(contact.getId())) {
            logger.warning("Invalid request: Contact ID is missing.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        int index = Collections.binarySearch(CONTACTS, contact, Comparator.comparing(Contact::getId));

        if (index < 0) {
            CONTACTS.add(contact);
            logger.info("Contact created with ID: " + contact.getId());
            return Response
                    .status(Response.Status.CREATED)
                    .location(URI.create(String.format("/api/v1.0/contacts/%s", contact.getId())))
                    .build();
        } else {
            logger.warning("Conflict: Contact with ID " + contact.getId() + " already exists.");
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateContact(@PathParam("id") Long id, Contact contact) {
        logger.info("PUT request received to update contact with ID: " + id);
        contact.setId(id);
        int index = Collections.binarySearch(CONTACTS, contact, Comparator.comparing(Contact::getId));

        if (index >= 0) {
            Contact updatedContact = CONTACTS.get(index);
            updatedContact.setPhone(contact.getPhone());
            CONTACTS.set(index, updatedContact);
            logger.info("Contact updated with ID: " + id);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            logger.warning("Contact not found with ID: " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteContact(@PathParam("id") Long id) {
        logger.info("DELETE request received to delete contact with ID: " + id);
        Contact contact = new Contact(id, null, null);
        int index = Collections.binarySearch(CONTACTS, contact, Comparator.comparing(Contact::getId));

        if (index >= 0) {
            CONTACTS.remove(index);
            logger.info("Contact deleted with ID: " + id);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } else {
            logger.warning("Contact not found with ID: " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}