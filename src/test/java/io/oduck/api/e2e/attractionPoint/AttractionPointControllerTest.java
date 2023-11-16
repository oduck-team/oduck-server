package io.oduck.api.e2e.attractionPoint;

import com.google.gson.Gson;
import io.oduck.api.domain.attractionPoint.dto.AttractionPointReqDto.*;
import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.mockMember.WithCustomMockMember;
import io.oduck.api.global.stub.MemberStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
class AttractionPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/attraction-points";

    @Nested
    @DisplayName("입덕포인트 생성")
    class PostAttractionPoint{

        @Test
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        @DisplayName("입덕포인트 생성 성공 시 status 200 반환")
        void postAttractionPoint() throws Exception {
            //given
            Member member = new MemberStub().getMember();
            Long animeId = 1L;
            List<AttractionElement> elementList = new ArrayList<>();
            elementList.add(AttractionElement.DRAWING);
            elementList.add(AttractionElement.STORY);

                AttractionPointReq postAttractionPoint = AttractionPointReq
                                                            .builder()
                                                            .animeId(animeId)
                                                            .attractionElements(elementList)
                                                            .build();

            String content = gson.toJson(postAttractionPoint);

            //when
            ResultActions actions = mockMvc.perform(
                    post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                        .content(content));

            //then
            actions
                    .andExpect(status().isOk())
                    .andDo(document("postAttractionPoint/success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    requestHeaders(
                                            headerWithName(HttpHeaders.COOKIE)
                                                    .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                                    .description("Header Cookie, 세션 쿠키")
                                    ),
                                    requestFields(attributes(key("title").value("Fields for AttractionPoint creation")),
                                            fieldWithPath("animeId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .attributes(field("constraints", "애니 아이디, NotNull, Min(1)"))
                                                    .description("애니 고유 식별 번호"),
                                            fieldWithPath("attractionElements")
                                                    .type(JsonFieldType.ARRAY)
                                                    .attributes(field("constraints", "DRAWING,  STORY,  MUSIC, CHARACTER, VOICE_ACTOR 리스트만 허용합니다. "))
                                                    .description("입덕포인트 리스트")
                                    ))
                    );
        }

    }

    @Nested
    @DisplayName("입덕포인트 존재 유무")
    class GetAttractionPoint {

        @Test
        @DisplayName("입덕포인트 존재 시 false")
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void checkAttractionPoint() throws Exception{
            //given
            Long animeId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                    get(BASE_URL + "/{animeId}", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
            );
            //then
            actions.andExpect(status().isOk())
                    .andDo(document("checkAttractionPoint/isCheck",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName(HttpHeaders.COOKIE)
                                            .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                            .description("Header Cookie, 세션 쿠키")
                            ),
                            pathParameters(
                                    parameterWithName("animeId")
                                            .description("애니 고유 식별자")
                            ),
                            responseFields(
                                    fieldWithPath("isAttractionPoint")
                                            .type(JsonFieldType.BOOLEAN)
                                            .description("입덕포인트 존재 시 true, 부재 시 false"))
                    ));

        }
    }

}