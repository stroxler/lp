package testingscala

// The _._1 and _._2 bear some discussion: the first _ is just the usual
// anonymous argument to a one-parameter lambda. The second one happens to be
// the first character in the tuple-indexing methods _1 and _2.
//
// The flatMap function flattens out the Lists of Acts into a single list (an
// ordinary map call would have produced a List of Lists)
class CompilationAlbum(override val title: String,
                       override val year: Int,
                       val tracksAndActs: (Track, List[Act])*)
    extends Album(title, year, Some(tracksAndActs.map(_._1).toList),
                  tracksAndActs.flatMap(_._2).distinct:_*)
