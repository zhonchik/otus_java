package ru.otus.model;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Indices(@Index(value = "url", type = IndexType.Unique))
public class Feed implements Serializable {
    @Id
    private long id;
    private String url;
    private Set<Long> chats;

    public static Feed NewFeed(String url, Set<Long> chats) {
        return new Feed(NitriteId.newId().getIdValue(), url, chats);
    }
}
