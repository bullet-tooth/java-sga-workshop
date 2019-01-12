package com.exonum.workshop;

import com.exonum.binding.storage.indices.ListIndex;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Utils {

  public static <T> Stream<T> streamOf(ListIndex<T> objecs) {
    return StreamSupport.stream(objecs.spliterator(), false);
  }

  public Utils() {
  }
}
