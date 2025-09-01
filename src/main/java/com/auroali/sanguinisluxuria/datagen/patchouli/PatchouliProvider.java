package com.auroali.sanguinisluxuria.datagen.patchouli;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class PatchouliProvider implements DataProvider {
    protected final FabricDataOutput output;
    protected final DataOutput.PathResolver assetsPathResolver;
    protected final DataOutput.PathResolver dataPathResolver;

    private final List<PatchouliJsonBook> books;

    protected PatchouliProvider(FabricDataOutput output) {
        this.output = output;
        this.assetsPathResolver = this.output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "patchouli_books");
        this.dataPathResolver = this.output.getResolver(DataOutput.OutputType.DATA_PACK, "patchouli_books");
        this.books = new ArrayList<>();
    }

    protected void register(PatchouliJsonBook book) {
        this.books.add(book);
    }

    protected abstract void generate();

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        this.generate();

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (PatchouliJsonBook book : this.books) {
            Path bookPath = this.dataPathResolver.resolveJson(book.getId().withSuffixedPath("/book"));
            futures.add(DataProvider.writeToPath(writer, book.toJson(), bookPath));

            for (PatchouliJsonBook.LangPack pack : book.getLangPacks()) {
                String prefix = book.getId().getPath() + "/" + pack.getLang() + "/";
                String categoryPrefix = prefix + "categories/";
                String entryPrefix = prefix + "entries/";

                for (PatchouliJsonCategory category : pack.getCategories()) {
                    Identifier categoryId = new Identifier(book.getId().getNamespace(), categoryPrefix + category.getId());
                    futures.add(DataProvider.writeToPath(writer, category.toJson(), this.assetsPathResolver.resolveJson(categoryId)));
                    for (PatchouliJsonEntry entry : category.getEntries()) {
                        Identifier id = new Identifier(book.getId().getNamespace(), entryPrefix + entry.getId());
                        futures.add(DataProvider.writeToPath(writer, entry.toJson(book.getId().getNamespace()), this.assetsPathResolver.resolveJson(id)));
                    }
                }
            }
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Patchouli Books";
    }
}
