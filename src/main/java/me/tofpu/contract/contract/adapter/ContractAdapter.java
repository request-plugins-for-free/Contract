package me.tofpu.contract.contract.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.user.User;

import java.io.IOException;
import java.util.UUID;

public class ContractAdapter extends TypeAdapter<Contract> {
    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(final JsonWriter out, final Contract value) throws IOException {
        out.beginObject();

        final User user = value.getEmployer();
        out.name("employer-name").value(user.getName());
        out.name("employer-unique-id").value(user.getUniqueId().toString());
        out.name("description").value(value.description());
        out.name("amount").value(value.getAmount());
        out.name("started-at").value(value.startedAt());
        out.name("length").value(value.length());

        out.endObject();
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     *
     * @return the converted Java object. May be null.
     */
    @Override
    public Contract read(final JsonReader in) throws IOException {
        in.beginObject();
        String employerName = "";
        UUID employerUniqueId = null;

        String description = "";
        double amount = 0;

        long startedAt = 0;
        long length = 0;

        while (in.hasNext()){
            switch (in.nextName()){
                case "employer-name":
                    employerName = in.nextString();
                    break;
                case "employer-unique-id":
                    employerUniqueId = UUID.fromString(in.nextString());
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "amount":
                    amount = in.nextDouble();
                    break;
                case "started-at":
                    startedAt = in.nextLong();
                    break;
                case "length":
                    length = in.nextLong();
                    break;
            }
        }

        in.endObject();
        return ContractFactory.create(employerUniqueId, employerName, length, amount, description);
    }
}
