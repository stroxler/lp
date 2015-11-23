crun: compile test run

run:
	R_PROFILE_USER='./proj-RProfile' R --no-save

compile:
	R -e 'devtools::document(); devtools::compile_dll()'

test: compile
	R -e 'devtools::test()'

# NOTE: if you want to use C++11, you can. You need to set the compile to the
#       line below. But for now, R uses -Wc++11, so it will give a bunch of
#       warnings. And also for now, CRAN won't accept C++11 code.
# R -e 'devtools::document(); Sys.setenv("PKG_CXXFLAGS"="-std=c++11"); devtools::compile_dll()'

clean:
	rm -rf src/*.o src/*.so src/RcppExports.cpp R/RcppExports.R
	rm -rf man/*
	echo '' > NAMESPACE
