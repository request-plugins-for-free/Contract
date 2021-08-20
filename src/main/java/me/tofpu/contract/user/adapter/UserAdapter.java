package me.tofpu.contract.user.adapter;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.review.factory.ContractReviewFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(final JsonWriter out, final User value) throws IOException {
        out.beginObject();

        out.name("name").value(value.name());
        out.name("unique-id").value(value.uniqueId().toString());

        out.name("current-contract");
        DataManager.GSON.toJson(value.currentContract(), Contract.class, out);

        out.name("total-rating").value(value.totalRating());

        out.endObject();
    }

    @Override
    public User read(final JsonReader in) throws IOException {
        in.beginObject();

        String name = "";
        UUID uuid = null;
        Contract currentContract = null;
        double totalRating = 0;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "name":
                    name = in.nextString();
                    break;
                case "unique-id":
                    uuid = UUID.fromString(in.nextString());
                    break;
                case "total-rating":
                    totalRating = in.nextDouble();
                    break;
                case "current-current":
                    currentContract = DataManager.GSON.getAdapter(Contract.class).read(in);
                    break;
            }
        }

        in.endObject();
        return UserFactory.create(name, uuid, currentContract, totalRating);
    }
}
