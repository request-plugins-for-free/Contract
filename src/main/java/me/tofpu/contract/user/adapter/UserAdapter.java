package me.tofpu.contract.user.adapter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.properties.stars.review.UserReview;
import me.tofpu.contract.user.properties.stars.review.factory.UserReviewFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<User> {
    @Override
    public void write(final JsonWriter out, final User value) throws IOException {
        out.beginObject();

        out.name("name").value(value.getName());
        out.name("unique-id").value(value.getUniqueId().toString());
        // TODO: CONTRACT ADAPTER
        out.name("current-contract");
        DataManager.GSON.toJson(value.currentContract(), Contract.class, out);

        out.name("total-rating").value(value.totalRating());

        out.name("ratedBy").beginArray();
        for (final UserReview userReview : value.ratedBy()){
            out.beginObject();

            out.name("name").value(userReview.getName());
            out.name("uniqueId").value(userReview.uniqueId().toString());
            out.name("rated").value(userReview.rated());
            out.name("description").value(userReview.description());

            out.endObject();
        }
        out.endArray();

        out.endObject();
    }

    @Override
    public User read(final JsonReader in) throws IOException {
        in.beginObject();

        String name = "";
        UUID uuid = null;
        Contract currentContract = null;
        double totalRating = 0;

        final List<UserReview> ratedBy = Lists.newArrayList();
        while (in.hasNext()){
            switch (in.nextName()){
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
                case "ratedBy":
                    in.beginArray();
                    String reviewName = "";
                    UUID reviewUniqueId = null;
                    double reviewRated = 0;
                    String reviewDescription = "";
                    while (in.hasNext()){
                        in.beginObject();

                        switch (in.nextName()){
                            case "name":
                                reviewName = in.nextString();
                                break;
                            case "uniqueId":
                                reviewUniqueId = UUID.fromString(in.nextString());
                                break;
                            case "rated":
                                reviewRated = in.nextDouble();
                                break;
                            case "description":
                                reviewDescription = in.nextString();
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + in.nextName());
                        }
                        ratedBy.add(UserReviewFactory.create(reviewName, reviewUniqueId, reviewRated, reviewDescription));

                        in.endObject();
                    }
                    in.endArray();
            }
        }

        in.endObject();
        return UserFactory.create(name, uuid, currentContract, totalRating, ratedBy);
    }
}
