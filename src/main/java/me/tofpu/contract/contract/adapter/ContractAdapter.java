package me.tofpu.contract.contract.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.review.factory.ContractReviewFactory;

import java.io.IOException;
import java.util.UUID;

public class ContractAdapter extends TypeAdapter<Contract> {

    @Override
    public void write(final JsonWriter out, final Contract value) throws IOException {
        out.beginObject();
        out.name("id").value(value.id().toString());

        out.name("employer-name").value(value.employerName());
        out.name("employer-unique-id").value(value.employerId().toString());

        out.name("contractor-name").value(value.contractorName());
        out.name("contractor-unique-id").value(value.contractorId().toString());

        final ContractReview review = value.review();
        out.name("review").beginArray().beginObject();
        out.name("rate").value(review.rate());
        out.name("description").value(review.description());
        out.endObject().endArray();

        out.name("description").value(value.description());
        out.name("amount").value(value.amount());
        out.name("started-at").value(value.startedAt());
        out.name("length").value(value.length());

        out.endObject();
    }

    @Override
    public Contract read(final JsonReader in) throws IOException {
        in.beginObject();
        UUID id = null;

        String employerName = "";
        UUID employerUniqueId = null;

        String contractorName = "";
        UUID contractorUniqueId = null;

        ContractReview review = null;

        String description = "";
        double amount = 0;

        long startedAt = 0;
        long length = 0;

        while (in.hasNext()){
            switch (in.nextName()){
                case "id":
                    id = UUID.fromString(in.nextString());
                    break;
                case "employer-name":
                    employerName = in.nextString();
                    break;
                case "employer-unique-id":
                    employerUniqueId = UUID.fromString(in.nextString());
                    break;
                case "contractor-name":
                    contractorName = in.nextString();
                    break;
                case "contractor-unique-id":
                    contractorUniqueId = UUID.fromString(in.nextString());
                    break;
                case "review":
                    in.beginArray();
                    in.beginObject();
                    int reviewRate = -1;
                    String reviewDescription = null;
                    while (in.hasNext()){
                        switch (in.nextName()){
                            case "rate":
                                reviewRate = in.nextInt();
                                break;
                            case "description":
                                if (in.peek() != JsonToken.NULL)
                                    reviewDescription = in.nextString();
                                else in.nextNull();
                                break;
                        }
                        review = ContractReviewFactory.create(reviewRate, reviewDescription);
                    }
                    in.endObject();
                    in.endArray();
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
        return ContractFactory.create(id, employerName, employerUniqueId, contractorName, contractorUniqueId, review, description, startedAt, length, amount);
    }
}
