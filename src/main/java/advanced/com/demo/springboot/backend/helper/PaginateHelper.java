package advanced.com.demo.springboot.backend.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginateHelper {

    public static Pageable pageable(Integer offset, Integer limit, String sortBy) {
        boolean asc = sortBy.startsWith("-");

        return asc ? new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending())
                : new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
    }

    public static Pageable limitPageable(Integer count , String sortBy) {
        if(count==0){
            return Pageable.unpaged();
        }
        boolean asc = sortBy.startsWith("-");

        return asc ? PageRequest.of(0, count,
                Sort.by(sortBy.substring(1)).descending())
                : PageRequest.of(0, count,Sort.by(sortBy));
    }

}
