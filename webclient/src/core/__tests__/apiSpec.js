import fetchMock from "fetch-mock";
import { Api, UnauthenticatedError, UnauthorizedError, BadRequestError, NotFoundRequestError } from "../api";

describe("#Api", () => {
    let api = new Api();
    afterEach(() => {
        fetchMock.restore();
    })

    it("throw UnauthenticatedError if status = 401", async () => {
        expect.assertions(1);
        fetchMock.mock("*", 401);
        await expect(api.get("/any")).rejects.toBeInstanceOf(UnauthenticatedError);
    })

    it("throw UnauthorizedError if status = 402", async () => {
        expect.assertions(1);
        fetchMock.mock("*", 402);
        await expect(api.get("/any")).rejects.toBeInstanceOf(UnauthorizedError);
    })

    it("throw BadRequestError if status = 400", async () => {
        expect.assertions(4);
        fetchMock.mock("*", { status: 400, body: { error: { message: "error here" } } });
        try {
            await api.get("/any");
        } catch (e) {
            expect(e).toBeInstanceOf(BadRequestError);
            expect(e.message).toEqual("Bad Request")
            expect(e.statusCode).toEqual(400)
            expect(e.body).toEqual({ error: { message: "error here" } })
        }
    })

    it("throw NotFoundRequestError if status = 404", async () => {
        expect.assertions(4);
        fetchMock.mock("*", { status: 404, body: { error: { message: "error here" } } });
        try {
            await api.get("/any");
        } catch (e) {
            expect(e).toBeInstanceOf(NotFoundRequestError);
            expect(e.message).toEqual("Not Found")
            expect(e.statusCode).toEqual(404)
            expect(e.body).toEqual(JSON.stringify({ error: { message: "error here" } }))
        }
    })

    it("request should serialize body object", async ()=>{
        fetchMock.mock("*", 200);
        await api.post("/api", {data:"1"});
        expect(fetchMock.lastCall()[1].body).toEqual(JSON.stringify({data:"1"}));
    })

    it("request should send body as text if it's text", async ()=>{
        fetchMock.mock("*", 200);
        await api.post("/api", "body text");
        expect(fetchMock.lastCall()[1].body).toEqual("body text");
    })

    it("request should send token if exists", async ()=>{
        const tokenStorage = {
            hasToken: jest.fn().mockReturnValue(true),
            getToken: jest.fn().mockReturnValue("abc"),
        }
        api = new Api(tokenStorage);

        fetchMock.mock("*", 200);
        await api.post("/api", "body text");

        expect(fetchMock.lastCall()[1].headers["Authorization"]).toEqual("Bearer abc");
    })

    it("login should not send token even token exists", async ()=>{
        const tokenStorage = {
            hasToken: jest.fn().mockReturnValue(true),
            getToken: jest.fn().mockReturnValue("abc"),
        }
        api = new Api(tokenStorage);

        fetchMock.mock("*", 200);
        await api.login("/api", "body text");

        expect(fetchMock.lastCall()[1].headers["Authorization"]).toEqual(undefined);
    })
})